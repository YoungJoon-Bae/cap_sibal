/*
  Copyright 2017, Google, Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/


// [START dialogflow_import_libraries]
// Imports the Google Cloud client library
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.api.gax.rpc.BidiStreamingCallable;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.cloud.speech.v1.WordInfo;
import com.google.common.util.concurrent.SettableFuture;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.TargetDataLine;
// [END dialogflow_import_libraries]


/**
 * DialogFlow API Detect Intent sample with text inputs.
 */
public class DetectIntentTexts {
	  public static String answer = null;
  // [START dialogflow_detect_intent_text]
  /**
   * Returns the result of detect intent with texts as inputs.
   *
   * Using the same `session_id` between requests allows continuation of the conversation.
   * @param projectId Project/Agent Id.
   * @param texts The text intents to be detected based on what a user says.
   * @param sessionId Identifier of the DetectIntent session.
   * @param languageCode Language code of the query.
   */
  public static void detectIntentTexts(String projectId, List<String> texts, String sessionId,
      String languageCode) throws Exception {
    // Instantiates a client
    try (SessionsClient sessionsClient = SessionsClient.create()) {
      // Set the session name using the sessionId (UUID) and projectID (my-project-id)
      SessionName session = SessionName.of(projectId, sessionId);
      System.out.println("Session Path: " + session.toString());

      // Detect intents for each text input
      for (String text : texts) {
        // Set the text (hello) and language code (en-US) for the query
        Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

        // Build the query with the TextInput
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        // Performs the detect intent request
        DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

        // Display the query result
        QueryResult queryResult = response.getQueryResult();

        System.out.println("====================");
        System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
        System.out.format("Detected Intent: %s (confidence: %f)\n",
            queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
        System.out.format("Fulfillment Text: '%s'\n", queryResult.getFulfillmentText());
      }
    }
  }
 
  // [END dialogflow_detect_intent_text]
  public static String streamingMicRecognize() throws Exception {
	 
	  ResponseObserver<StreamingRecognizeResponse> responseObserver = null;
	    try (SpeechClient client = SpeechClient.create()) {
	    	
	      responseObserver =
	          new ResponseObserver<StreamingRecognizeResponse>() {
	            ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

	            public void onStart(StreamController controller) {}

	            public void onResponse(StreamingRecognizeResponse response) {
	              responses.add(response);
	            }

	            public void onComplete() {
	              for (StreamingRecognizeResponse response : responses) {
	                StreamingRecognitionResult result = response.getResultsList().get(0);
	                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
	                System.out.printf("Transcript : %s\n", alternative.getTranscript());
	                 answer = alternative.getTranscript();
	              }
	            }

	            public void onError(Throwable t) {
	              System.out.println(t);
	            }
	          };

	      ClientStream<StreamingRecognizeRequest> clientStream =
	          client.streamingRecognizeCallable().splitCall(responseObserver);

	      RecognitionConfig recognitionConfig =
	          RecognitionConfig.newBuilder()
	              .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
	              .setLanguageCode("ko-KR")
	              .setSampleRateHertz(16000)
	              .build();
	      StreamingRecognitionConfig streamingRecognitionConfig =
	          StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).build();

	      StreamingRecognizeRequest request =
	          StreamingRecognizeRequest.newBuilder()
	              .setStreamingConfig(streamingRecognitionConfig)
	              .build(); // The first request in a streaming call has to be a config

	      clientStream.send(request);
	      // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true,
	      // bigEndian: false
	      AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
	      DataLine.Info targetInfo =
	          new Info(
	              TargetDataLine.class,
	              audioFormat); // Set the system information to read from the microphone audio stream

	      if (!AudioSystem.isLineSupported(targetInfo)) {
	        System.out.println("Microphone not supported");
	        System.exit(0);
	      }
	      // Target data line captures the audio stream the microphone produces.
	      TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
	      targetDataLine.open(audioFormat);
	      targetDataLine.start();
	      System.out.println("Start speaking");
	      long startTime = System.currentTimeMillis();
	      // Audio Input Stream
	      AudioInputStream audio = new AudioInputStream(targetDataLine);
	      while (true) {
	        long estimatedTime = System.currentTimeMillis() - startTime;
	        byte[] data = new byte[6400];
	        audio.read(data);
	        if (estimatedTime > 10000) { // 60 seconds
	          System.out.println("Stop speaking.");
	          targetDataLine.stop();
	          targetDataLine.close();
	          break;
	        }
	        request =
	            StreamingRecognizeRequest.newBuilder()
	                .setAudioContent(ByteString.copyFrom(data))
	                .build();
	        clientStream.send(request);
	      }
	    } catch (Exception e) {
	      System.out.println(e);
	    }
	    responseObserver.onComplete();
	    return answer;
		}
  // [START run_application]
  
  
  
  public static void main(String[] args) throws Exception {
	  
    ArrayList<String> texts = new ArrayList<>();
    String projectId = "capstoneproject-ycqucq";
    String sessionId = UUID.randomUUID().toString();
    String languageCode = "ko-KR";
    Scanner scan = new Scanner(System.in);
    String e = "";
    
    try {
    	while(true) {
        	while(!e.equals("end")) {
            	texts.clear();
            	System.out.print("다음 문장을 입력하세요:");
            	//e = streamingMicRecognize(); 음성
            	e = scan.nextLine(); //타이핑
            	texts.add(e);
            	detectIntentTexts(projectId, texts, sessionId, languageCode);
            }
        	texts.clear();
        	System.out.println("주문 완료 다음 손님 입장");
        	System.out.print("다음 문장을 입력하세요:");
        	e = scan.nextLine();
        	texts.add(e);
        	sessionId = UUID.randomUUID().toString();
        } 
    }
    catch(Exception e1){
    	System.out.println(e1);
    }
    
  }
}
