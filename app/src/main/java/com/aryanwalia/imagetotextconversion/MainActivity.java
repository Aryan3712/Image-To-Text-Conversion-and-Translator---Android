    package com.aryanwalia.imagetotextconversion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button capture_button,find_button;
    ImageView imageView;
    TextView restView,developed_textView;
    RadioGroup radioGroup;
    RadioButton radio_one,radio_two;
    private static final String FILE_NAME = "extracted_text.txt";
    private static String FILE_OUT = "file_out.txt";
    public static final String EXTRA_LIST_ONE = "com.aryanwalia.imagetotextconversion.EXTRA_LIST";
    public static final String EXTRA_LIST_TWO = "com.aryanwalia.imagetotextconversion.EXTRA_LIST_TWO";
    public static final String EXTRA_LIST_THREE = "com.aryanwalia.imagetotextconversion.EXTRA_LIST_THREE";
    public static final String EXTRA_LIST_FOUR = "com.aryanwalia.imagetotextconversion.EXTRA_LIST_FOUR";
    public static final String EXTRA_LIST_FIVE= "com.aryanwalia.imagetotextconversion.EXTRA_LIST_FIVE";
    public static final String EXTRA_LIST_SIX= "com.aryanwalia.imagetotextconversion.EXTRA_LIST_SIX";
    public static final String RADIO_ID = "com.aryanwalia.imagetotextconversion.RADIO_ID";
    public static final String RADIO_TWO = "com.aryanwalia.imagetotextconversion.RADIO_TWO";

    public List<String> matched_words = new ArrayList<>();
    public List<String> synonym_words = new ArrayList<>();
    public int radioId;
    public int imageSet=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        capture_button = findViewById(R.id.capture_button);
        imageView = findViewById(R.id.imageView);
        restView = findViewById(R.id.restView);
        find_button = findViewById(R.id.find_button);
        radioGroup = findViewById(R.id.radioGroup);
        radio_one = findViewById(R.id.radio_one);
        radio_two = findViewById(R.id.radio_two);

        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //grant the permission
            requestPermissions(new String[] {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }




        radioGroup.setOnCheckedChangeListener( (group, checkedId) -> {
            switch(checkedId){
                case R.id.radio_one:
                    find_button.setOnClickListener(v -> {
                        if(matched_words.size()==0 && imageSet ==1){
                           openHelpSection();
                           this.runOnUiThread(() -> Toast.makeText(MainActivity.this,"No Word Found",Toast.LENGTH_SHORT).show());

                        }
                        else if((imageSet == 1) && (matched_words.size() >0)) {
                            openListActivityForTranslation();
                        } else
                            this.runOnUiThread(() -> Toast.makeText(MainActivity.this,"Scan Image First",Toast.LENGTH_SHORT).show());

                    });

                break;
                case R.id.radio_two:
                    radioId = R.id.radio_two;
                    find_button.setOnClickListener(v -> {
                        if (matched_words.size() == 0 && imageSet == 1) {
                            Toast.makeText(this, "NO WORD FOUND", Toast.LENGTH_SHORT).show();
                            openHelpSection();
                        }
                        else if (imageSet == 0)
                            Toast.makeText(this, "Scan Image First", Toast.LENGTH_SHORT).show();
                        else
                            openListActivityForSynonyms();
                    });
                break;
                default: find_button.setOnClickListener(v -> {
                    Toast.makeText(this, "NO OPTION SELECTED", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void openHelpSection() {
        Intent intent = new Intent(this,mailActivity.class);
        startActivity(intent);
    }

    private void openListActivityForSynonyms() {

        ArrayList<String> synAnts = new ArrayList<>();
        String synonyms_heading ="Word     Synonym     Antonym";
        synAnts.add(synonyms_heading);
        synAnts.addAll(synonym_words);
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra(RADIO_ID,R.id.radio_two);
        intent.putStringArrayListExtra(EXTRA_LIST_FOUR,synAnts);
        intent.putStringArrayListExtra(EXTRA_LIST_FIVE,synAnts);
        startActivity(intent);
    }

    private void openListActivityForTranslation() {
        ArrayList<String> translations = new ArrayList<>();
        String translation_heading = "Complex Word     Hindi     Punjabi     Gujarati     Arabic";
        translations.add(translation_heading);
        translations.addAll(matched_words);
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra(RADIO_ID,R.id.radio_one);
        intent.putStringArrayListExtra(EXTRA_LIST_ONE,translations);
        intent.putStringArrayListExtra(EXTRA_LIST_TWO,translations);
        startActivity(intent);
    }



    public void findText(View view) {
        radio_one.setChecked(false);
        radio_two.setChecked(false);
        CropImage.activity().start(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
                imageSet++;

                BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if(!recognizer.isOperational()){
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
                }
                else{
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<items.size();i++){
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }

                    saveIntoFile(sb.toString());
                    readAndMatch();
                }
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(this,"Possible Error is: "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void saveIntoFile(String readText)  {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            String words[] = readText.split(" ");
            for(String word:words){
                fos.write(word.toUpperCase().getBytes());
                fos.write(System.lineSeparator().getBytes());
            }
            Toast.makeText(this,"Saved to "+getFilesDir() +"/"+FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void readAndMatch() {

        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while((text = br.readLine()) != null){
                sb.append(text).append(" ");
            }
            findFromDataBase(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void findFromDataBase(String data) {

        List<String> already_one = new ArrayList<>();
        List<String> already_two = new ArrayList<>();

        String wordsString[] = data.split(" ");
        for (String w : wordsString) {
            String line;
            try {
                InputStream is1 = getResources().openRawResource(R.raw.data);
                BufferedReader br1 = new BufferedReader(new InputStreamReader(is1, StandardCharsets.UTF_8));
                while ((line = br1.readLine()) != null) {
                    //Split by ','
                    String[] tokens = line.split(",");
                    if (w.equals(tokens[0]) && !already_one.contains(tokens[0])) {
                        matched_words.add(line.replaceAll(",", "     "));
                        already_one.add(tokens[0]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream is2 = getResources().openRawResource(R.raw.data2);
                BufferedReader br2 = new BufferedReader(new InputStreamReader(is2, StandardCharsets.UTF_8));
                while((line = br2.readLine())!=null){
                    //Split by ','
                    String[] tokens2 = line.split(",");
                    if(w.equals(tokens2[0]) && !already_two.contains(tokens2[0])){
                        synonym_words.add(line.replaceAll(",","     "));
                        already_two.add(tokens2[0]);
                    }
                }

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /*private void openListActivity() {

        ArrayList<String> translations = new ArrayList<>();
        String translation_heading = "Complex Word     Hindi     Punjabi     Gujarati     Arabic";
        translations.add(translation_heading);
        translations.addAll(matched_words);

        intent.putStringArrayListExtra(EXTRA_LIST_ONE,translations);
        intent.putStringArrayListExtra(EXTRA_LIST_TWO,translations);

        ArrayList<String> synAnts = new ArrayList<>();
        String synonyms_heading ="Word     Synonym     Antonym";
        synAnts.add(synonyms_heading);
        synAnts.addAll(synonym_words);

        Intent intent = new Intent(this,ListActivity.class);





        intent.putStringArrayListExtra(EXTRA_LIST_FOUR,synAnts);
        intent.putStringArrayListExtra(EXTRA_LIST_FIVE,synAnts);
        startActivity(intent);

        Toast.makeText(MainActivity.this,"Select an option",Toast.LENGTH_LONG).show();
    }*/

}

