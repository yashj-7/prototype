package android.example.prototype;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextToSpeech tts;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    ArrayList<String> result;
    int ans;
    int c;
    int[] a;
    int[] b;
    int i = 0;
    ImageView imgView;
    TextView txt1;
    TextView txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView)findViewById(R.id.mic);
        txt1 = findViewById(R.id.textView1);
        txt2 = findViewById(R.id.textView2);

            Random r = new Random();

            a = new int[10];
            b = new int[10];
            for(int i=0;i<10;i++){
                a[i]=r.nextInt(100)+1;
                b[i]=r.nextInt(100)+1;
            }

            generate(0);
            i++;


            imgView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                            Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                    try {
                        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);

                    }
                    catch (Exception e) {
                        Toast.makeText(MainActivity.this, " " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                try{
                    ans=  Integer.parseInt(result.get(0));
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "NAN", Toast.LENGTH_SHORT).show();
                }
                Log.v("ans",""+ans);
                if(ans==c){
                    tts.speak("correct",TextToSpeech.QUEUE_FLUSH,null);
                    Toast.makeText(MainActivity.this, "correct", Toast.LENGTH_SHORT).show();
                }else{
                    tts.speak("incorrect",TextToSpeech.QUEUE_FLUSH,null);
                    Toast.makeText(MainActivity.this, "incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        }
        generate(i);
        i++;
    }


    public void generate(int i){
        if(i>=10){
            Toast.makeText(MainActivity.this, "Over", Toast.LENGTH_SHORT).show();
            imgView.setClickable(false);
            return;
        }
        txt1.setText(String.valueOf(a[i]));
        txt2.setText(String.valueOf(b[i]));
        c = a[i]*b[i];
        Log.v("cnn",""+c);
        String x = String.valueOf(a[i]) + "multiply" + String.valueOf(b[i]);
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                    tts.setSpeechRate(1.0f);
                    tts.speak(x, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });

    }

}