package com.bruce.dice.travelapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;

import java.util.ArrayList;
import java.util.List;

public class ReplyActivity extends AppCompatActivity {
    EditText etSendMessage;
    Button btnSendMessage;
    TextView tvReplyMessage;
    String userID = "123456";

    List<TextMessage> conversation;
    SmartReplyGenerator smartReplyGenerator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartreply);

        etSendMessage = findViewById(R.id.etSendMessage);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        tvReplyMessage = findViewById(R.id.tvReplayMessage);

        conversation = new ArrayList<>();
        smartReplyGenerator = SmartReply.getClient();

        btnSendMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String message = etSendMessage.getText().toString().trim();
                conversation.add(TextMessage.createForRemoteUser(message,System.currentTimeMillis(),userID));

                SmartReplyGenerator smartReply = SmartReply.getClient();
                smartReply.suggestReplies(conversation)
                        .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                            @Override
                            public void onSuccess(SmartReplySuggestionResult result) {
                                if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                                    // The conversation's language isn't supported, so
                                    // the result doesn't contain any suggestions.
                                    tvReplyMessage.setText("No Reply");
                                } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                                    // Task completed successfully
                                    // ...
                                    String reply ="";
                                    for(SmartReplySuggestion suggestion:result.getSuggestions()){
                                        reply= reply + suggestion.getText() + "\n";
                                    }
                                    tvReplyMessage.setText(reply);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                                tvReplyMessage.setText("Error" + e.getMessage());
                            }
                        });
            }
        });


    }
}
