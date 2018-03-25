package com.willemthewalrus.humemon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by William on 3/24/2018.
 */

public class photoManipulation {

    //camera photo filepath String
    public static String mCurrentPhotoPath;

    //creates a file to store our camera photo in
    public static File createImageFile(Context context) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //converts a file to a byte array, expects filepath in a String format
    public static byte[] fileToByteArray(String filepath) {
        File workingfile = new File(filepath);

        //make sure the photo that we took exists in storage
        if (workingfile.exists()) {

            //set our bitmap equal to the file in storage
            Bitmap ourmap = BitmapFactory.decodeFile(workingfile.getAbsolutePath());

            //compress our bitmap into a smaller


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ourmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] ar = byteArrayOutputStream.toByteArray();
            return ar;
        } else {
            return null;
        }
    }

    //draws a humemon over a default humemon body based off of what is returned from the vision API call
    public static Bitmap drawHumemon(Bitmap map, List<EntityAnnotation> traits, humemonObject humemon,Context context) {


        int cowcount = 0;
        int ladycount = 0;
        int gymcount = 0;
        int mermaidcount = 0;

        for (int i = 0; i < traits.size(); i++) {


            //see if descriptions are associated with a recorded trait
            if(humemon.getTraitmap().containsKey(traits.get(i).getDescription())) {

                String currentType = humemon.getTraitmap().get(traits.get(i).getDescription());
                Log.i("currentdescription", traits.get(i).getDescription()+ " "+ currentType);

                if (currentType.equals("cowtaur")) {
                    cowcount++;

                } else if (currentType.equals("beanlady")) {
                    ladycount++;
                } else if (currentType.equals("gymbro")) {
                    gymcount++;
                } else if (currentType.equals("reversemermaid")) {
                    mermaidcount++;
                }
            }
            //if there is no associated trait, assign a point randomly
            else{
                Random generator = new Random();
                int picker = generator.nextInt(4)+1;
                Log.i("currentdescription", traits.get(i).getDescription() + " " + Integer.toString(picker));
                if(picker == 1){
                    cowcount++;
                }
                else if(picker == 2){
                    ladycount++;
                }
                else if(picker == 3){
                    gymcount++;
                }
                else if( picker == 4){
                    mermaidcount++;
                }


            }


        }

        //set type based off of the category with the most traits in this api response
        int max = cowcount;
        humemon.setType("cowtaur");
        if(max < ladycount) {
            max = ladycount;
            humemon.setType("beanlady");
        }

        if (max < gymcount) {
            max = gymcount;
            humemon.setType("gymbro");
        }
        if (max < mermaidcount) {
            max = mermaidcount;
            humemon.setType("reversemermaid");
        }

        //scale the face bitmap to a default size
        Log.i("type", humemon.getType());
        map = Bitmap.createScaledBitmap(map, 175, 175, false);

        //place the face on the image body using canvas
        if(humemon.getType().equals("reversemermaid")){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap reversemermaid = BitmapFactory.decodeResource(context.getResources(), R.drawable.reverse_mermaid,options);

            reversemermaid = Bitmap.createScaledBitmap(reversemermaid,800, 800, false);
            Canvas creature = new Canvas(reversemermaid);

            creature.drawBitmap(map, 300, 30, null);


            return reversemermaid;
        }


        else if(humemon.getType().equals("cowtaur")) {


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap cowtaur = BitmapFactory.decodeResource(context.getResources(), R.drawable.cowtaur,options);
            cowtaur = cowtaur.copy(Bitmap.Config.ARGB_8888, true);
            cowtaur = Bitmap.createScaledBitmap(cowtaur, 800, 800, false);

            Canvas creature = new Canvas(cowtaur);

            creature.drawBitmap(map, 410, 40, null);


            return cowtaur;
        }

        else if(humemon.getType().equals("gymbro")){

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap gymbro = BitmapFactory.decodeResource(context.getResources(), R.drawable.gym_bro,options);

            gymbro = Bitmap.createScaledBitmap(gymbro, 800, 800, false);

            Canvas creature = new Canvas(gymbro);

            creature.drawBitmap(map, 250, 25, null);


            return gymbro;
        }

        else if(humemon.getType().equals("beanlady")){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap beanlady = BitmapFactory.decodeResource(context.getResources(), R.drawable.bean_lady,options);
            beanlady = Bitmap.createScaledBitmap(beanlady, 800, 800, false);
            Canvas creature = new Canvas(beanlady);
            creature.drawBitmap(map,300 , 75, null);
            return beanlady;
        }
        else{
            Log.i("error", "type not found");
            return map;
        }



    }




}
