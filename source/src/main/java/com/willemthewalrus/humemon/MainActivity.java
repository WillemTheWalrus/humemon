package com.willemthewalrus.humemon;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.willemthewalrus.humemon.photoManipulation.fileToByteArray;
import static com.willemthewalrus.humemon.photoManipulation.mCurrentPhotoPath;

public class MainActivity extends AppCompatActivity {


    final int REQUEST_TAKE_PHOTO = 69;
    final int REQUEST_CROP = 22;
    public static List<EntityAnnotation> responses;
    public static humemonObject currentHumemon;
    public Bitmap face;
    public static Bitmap critter;
    public static boolean firstvisit = true;
    public static boolean needQuotes = false;
    public static int quoteNum = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView quotes = findViewById(R.id.quotes);

        //if we are returning to this activity, we will branch here
        if(!firstvisit){

            ImageView img = findViewById(R.id.imageView);
            img.setImageBitmap(critter);
            Button button = findViewById(R.id.takePicandCrop);
            button.setVisibility(View.GONE);
            Button booty = findViewById(R.id.fight);
            booty.setVisibility(View.VISIBLE);
            Button stats = findViewById(R.id.viewstats);
            stats.setVisibility(View.VISIBLE);
            TextView textView = findViewById(R.id.introtext);
            textView.setVisibility(View.GONE);
        }
        if (needQuotes){
            quotes.setVisibility(View.VISIBLE);
            quotes.setText(humemonObject.philisophicalquotes[quoteNum]);
        }


    }

    /*
     * starts the fighting activity when button is clicked
     */
    public void startFight(View view){
        Intent intent = new Intent(this, fight_screen.class );
        startActivity(intent);
    }

    /*
     * Restarts the whole application when reset button is pressed
     */
    public void reset(View view){
        firstvisit = true;
        needQuotes = false;
        quoteNum = -1;

        Intent intent= getIntent();
        finish();
        startActivity(intent);

    }

    /*
     * switches the view to the Stats page activity
     */
    public void showStats(View view) {
        Intent intent = new Intent(this, stat_screen.class);
        startActivity(intent);

    }

    /*
     * Dispatches a take picture intent when a button is pressed
     */
    public void onClick(View view){
        dispatchTakePictureIntent();
    }

    /*
     * Displays the User's Humemon when a button is pressed
     */
    public void showHumemon(View view){
        initializeHumemon();
        Button butt = findViewById(R.id.fight);
        butt.setVisibility(View.VISIBLE);
        Button stats = findViewById(R.id.viewstats);
        stats.setVisibility(View.VISIBLE);
        Button create = findViewById(R.id.takePicandCrop);
        create.setVisibility(View.GONE);
        Button hum = findViewById(R.id.showhumemon);
        hum.setVisibility(View.GONE);
    }

    /*
     * creates a humemon from the returned label annotations and cropped image
     */
    private void initializeHumemon(){

        //creates a humemon object when this is the first visit to the Main Activity
        if(firstvisit) {

            //create a humemon object with standard stats
            currentHumemon = new humemonObject(0, 200, 0, 0, 100,true);

            //get the humemon image with cropped face attached and set to humemon stored bitmap
            critter = photoManipulation.drawHumemon(face, responses, currentHumemon, getApplicationContext());
            currentHumemon.setBitmap(critter);

            //check to see which type of humemon it is to set up basic attacks and buff
            if(currentHumemon.getType().equals("reversemermaid")){
                humemonUsers.setupReverseMermaid(currentHumemon);
            }
            else if(currentHumemon.getType().equals("cowtaur")){
                humemonUsers.setupCowtaur(currentHumemon);
            }
            else if(currentHumemon.getType().equals("gymbro")){
                humemonUsers.setupGymBro(currentHumemon);
            }
            else if(currentHumemon.getType().equals("beanlady")){
                humemonUsers.setupBeanLady(currentHumemon);
            }
            else{
                Log.i("humemon setup","type not recognized");
            }

            //add additional skill points based off of the google API Cloud Vision response
            for (int i = 0; i < responses.size(); i++) {
                String description = responses.get(i).getDescription();
                float score = responses.get(i).getScore() * 10;

                //iterate through responses to see if they contain a stored response that matches up with a creature type
                if (currentHumemon.getTraitmap().containsKey(description)) {
                    String mapKey = currentHumemon.getTraitmap().get(description);


                    if (mapKey.equals("reversemermaid")) {
                        currentHumemon.setSpeed(currentHumemon.getSpeed() + (int) (score));
                    } else if (mapKey.equals("cowtaur")) {
                        currentHumemon.setDefense(currentHumemon.getDefense() + (int) (score));
                    } else if (mapKey.equals("beanlady")) {
                        currentHumemon.setHealth(currentHumemon.getHealth() + (int) (score));
                    } else if (mapKey.equals("gymbro")) {
                        currentHumemon.setDamage(currentHumemon.getDamage() + (int) (score));
                    }

                }
            }
        }

        //display the humemon in the imageView
        currentHumemon.setBitmap(critter);
        ImageView img = findViewById(R.id.imageView);
        img.setImageBitmap(critter);

    }

    /*
    code referenced from: https://developer.android.com/training/camera/photobasics.html
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = photoManipulation.createImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.willemthewalrus.humemon.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //code referenced from https://stackoverflow.com/questions/15228812/crop-image-in-android
    private void performCrop(Uri picUri){
        try {
            Intent cropintent = new Intent("com.android.camera.action.CROP");
            cropintent.setDataAndType(picUri, "image/*");
            cropintent.putExtra("crop", true);
            cropintent.putExtra("aspectX", 1);
            cropintent.putExtra("aspectY", 1);
            cropintent.putExtra("outputX", 128);
            cropintent.putExtra("outputY", 128);
            cropintent.putExtra("return-data", true);
            cropintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(cropintent, REQUEST_CROP);
        } catch(ActivityNotFoundException e){
            Log.i("crop", "cropnotfound");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            //grab the URI address from the file that has our picture
            File ourphoto = new File(mCurrentPhotoPath);
            Uri uri = FileProvider.getUriForFile(this, "com.willemthewalrus.humemon.fileprovider", ourphoto );

            if(uri != null){
                //send off a crop intent
                performCrop(uri);
            }
            //get responses from the cloud vision api using our photo file path
            new getResponse().execute(mCurrentPhotoPath, keyClass.key);
        }
        //code pulled from https://stackoverflow.com/questions/15228812/crop-image-in-android
        else if(requestCode == REQUEST_CROP && resultCode == RESULT_OK){
            if(data != null){
                //set our face bitmap from the bitmap returned in the intent
                Bundle extras = data.getExtras();
                Bitmap map = extras.getParcelable("data");
                face = map;
                TextView intro = findViewById(R.id.introtext);
                intro.setVisibility(View.GONE);
                //hide the take picture button if we already have responses
                if(responses != null){
                    Button butt = findViewById(R.id.showhumemon);
                    butt.setVisibility(View.VISIBLE);
                    Button butty = findViewById(R.id.takePicandCrop);
                    butty.setVisibility(View.GONE);

                }

            }
        }
    }

    //class to get image descriptions
    class getResponse extends AsyncTask<String, String, BatchAnnotateImagesResponse>
    {
        String path;
        String key;

        protected void onPreExecute(){}

        @Override
        //code taken from class project whatisit
        protected BatchAnnotateImagesResponse doInBackground(String... args) {

            path = args[0];
            key = args[1];
            if (fileToByteArray(path) != null) {

                //encode a byte array into our image object
                Image inputIMG = new Image();
                inputIMG.encodeContent(fileToByteArray(path));


                //use the label detection feature of the cloud vision api
                Feature label = new Feature();
                label.setType("LABEL_DETECTION");
                label.setMaxResults(15);


                //create a image request with our stored file
                AnnotateImageRequest imageRequest = new AnnotateImageRequest();
                imageRequest.setImage(inputIMG);
                imageRequest.setFeatures(Collections.singletonList(label));

                VisionRequestInitializer initializer = new VisionRequestInitializer(key);
                Vision myvision = new Vision.Builder(
                        new NetHttpTransport(),
                        new AndroidJsonFactory(),
                        null).setVisionRequestInitializer(initializer).build();


                BatchAnnotateImagesRequest requestBatch = new BatchAnnotateImagesRequest();
                try {

                    requestBatch.setRequests(Collections.singletonList(imageRequest));

                    requestBatch.setRequests(Collections.singletonList(imageRequest));
                    BatchAnnotateImagesResponse response = myvision.images()
                            .annotate(requestBatch)
                            .setDisableGZipContent(true)
                            .execute();
                    return response;

                } catch (IOException e) {
                    Log.i("vision", "IOexception in getResponse");
                    return null;
                }


            } else {
                Log.i("vision", "null filepath");
                return null;
            }
        }

        protected void onPostExecute(BatchAnnotateImagesResponse result){
           responses = result.getResponses().get(0).getLabelAnnotations();

           //display the showhumemon button and hide the takepic button if we already have a cropped photo set

            if(critter != null){
                Button butt = findViewById(R.id.showhumemon);
                butt.setVisibility(View.VISIBLE);
                Button booty = findViewById(R.id.takePicandCrop);
                booty.setVisibility(View.GONE);
            }

        }
    }

}
