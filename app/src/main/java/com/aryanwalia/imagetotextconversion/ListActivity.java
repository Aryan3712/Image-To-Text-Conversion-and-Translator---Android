package com.aryanwalia.imagetotextconversion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;





import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;



public class ListActivity extends AppCompatActivity {


    //giving name to pdf
    String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
    //path to pdf file
    String filePath = Environment.getExternalStorageDirectory() + "/" + fileName + ".doc";

    private ListView output_text_list;
    private ArrayAdapter<String> wordAdapter;
    private TextView textView;
    int radio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        textView = findViewById(R.id.heading);
        int radioOne = 0;
        int radioTwo = 0;

        Intent intent = getIntent();
        int radioId = intent.getIntExtra(MainActivity.RADIO_ID,0);
        List<String> toWrite_matched_words;
        toWrite_matched_words = intent.getStringArrayListExtra(MainActivity.EXTRA_LIST_ONE);
        List<String> toWrite_synonyms_words;
        toWrite_synonyms_words = intent.getStringArrayListExtra(MainActivity.EXTRA_LIST_FOUR);

        switch (radioId){
            case R.id.radio_one:{ initTranslations(toWrite_matched_words);  radio = R.id.radio_one;}
                break;
            case R.id.radio_two:{initSynonyms(toWrite_synonyms_words);  radio = R.id.radio_two;}
                break;
            default: Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.pdf_button:{
                save();
                Toast.makeText(this,fileName+".doc"+"\n is saved to\n"+filePath,Toast.LENGTH_LONG).show();
            }

                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void save() {
        switch(radio){
            case R.id.radio_one: saveDOCTranslations();
            break;
            case R.id.radio_two: saveDOCSynonyms();
            break;
            default:Toast.makeText(this,"Unable to Create",Toast.LENGTH_LONG).show();
        }



    }


    private void saveDOCTranslations() {


        Intent intent = getIntent();
        List<String> saveDoc = new ArrayList<>();
        saveDoc.addAll(intent.getStringArrayListExtra(MainActivity.EXTRA_LIST_TWO));
        saveDoc.remove(0);
        writeTranslations(saveDoc, fileName, filePath);

    }

    private void saveDOCSynonyms() {

        Intent intent = getIntent();
        List<String> saveDoc = new ArrayList<>();
        saveDoc.addAll(intent.getStringArrayListExtra(MainActivity.EXTRA_LIST_FIVE));
        saveDoc.remove(0);
        writeSynonymsAntonyms(saveDoc,fileName,filePath);

    }


       /* File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            int c=1;
            for(String record:toWrite){
                String d[] = record.split("     ");

                    fos.write(("Complex Word"+" "+(c++)+" "+" -> "+d[0]).getBytes());
                    fos.write(System.lineSeparator().getBytes());
                    fos.write(("Hindi           "+" -> "+d[1]).getBytes());
                    fos.write(System.lineSeparator().getBytes());
                    fos.write(("Punjabi         "+" -> "+d[2]).getBytes());
                    fos.write(System.lineSeparator().getBytes());
                    fos.write(("Gujarati         "+" -> "+d[3]).getBytes());
                    fos.write(System.lineSeparator().getBytes());
                    fos.write(("Arabic          "+" -> "+d[4]).getBytes());
                    fos.write(System.lineSeparator().getBytes());
                    fos.write(System.lineSeparator().getBytes());

            }

        }
        catch (IOException e) {
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
        }*/


    private void writeSynonymsAntonyms(List<String> toWrite,String fileName,String filePath) {

        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            int c=1;
            for(String record:toWrite){
                String d[] = record.split("     ");
                fos.write(("Word: "+(c++)+"   "+"  "+" -> "+d[0]).getBytes());
                fos.write(System.lineSeparator().getBytes());
                fos.write(("Synonym:     "+" -> "+d[1]).getBytes());
                fos.write(System.lineSeparator().getBytes());
                fos.write(("Antonym:     "+" -> "+d[2]).getBytes());
                fos.write(System.lineSeparator().getBytes());
                fos.write(System.lineSeparator().getBytes());
            }

        }
        catch (IOException e) {
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

    private void writeTranslations(List<String> toWrite, String fileName, String filePath) {

        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            int c=1;
            for(String record:toWrite){
                String d[] = record.split("     ");

                fos.write(("Complex Word"+" "+(c++)+" "+" -> "+d[0]).getBytes());
                fos.write(System.lineSeparator().getBytes());
                fos.write(("Hindi           "+" -> "+d[1]).getBytes());
                fos.write(System.lineSeparator().getBytes());
                fos.write(("Punjabi         "+" -> "+d[2]).getBytes());
                fos.write(System.lineSeparator().getBytes());
                fos.write(("Gujarati         "+" -> "+d[3]).getBytes());
                fos.write(System.lineSeparator().getBytes());
                fos.write(("Arabic          "+" -> "+d[4]).getBytes());
                fos.write(System.lineSeparator().getBytes());
                fos.write(System.lineSeparator().getBytes());
            }

        }
        catch (IOException e) {
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


    private void initTranslations(List<String> toWrite_matched_words) {

        List<String> trn = new ArrayList<>();
        trn.addAll(toWrite_matched_words);

        textView.setText("Complex Words");
        output_text_list = findViewById(R.id.list_matched_words);
        wordAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, trn);
        output_text_list.setAdapter(wordAdapter);


    }

    private void initSynonyms(List<String> toWrite_synonyms_words) {

        textView.setText("Synonyms/Antonyms");
        output_text_list = findViewById(R.id.list_matched_words);

        wordAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,toWrite_synonyms_words);
        output_text_list.setAdapter(wordAdapter);

    }


}

