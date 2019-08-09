package com.mobile.anvce.baking.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.mobile.anvce.baking.models.Step;

/**
 * Models Step for Retrofit2 library
 *
 * @author Venkatesh Maganahalli
 */
@Entity(tableName = "STEP")
public class DbStep implements Parcelable {

    public final static String PATTERN = "(^[0-9]+\\. )";
    public final static Parcelable.Creator<DbStep> CREATOR = new Creator<DbStep>() {
            @SuppressWarnings({
                    "unchecked"
            })
            public DbStep createFromParcel(Parcel in) {
            return new DbStep(in);
        }

            public DbStep[] newArray(int size) {
            return (new DbStep[size]);
        }

        };

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int stepId;
    private String shortDescription = "";
    private String description = "";
    private String videoURL = "";
    private String thumbnailURL = "";
    private int recipeId;
    public DbStep(int stepId, String shortDescription, String description, String videoURL, String thumbnailURL, int recipeId) {
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }
    @Ignore
    public DbStep() {
    }

    protected DbStep(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.shortDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.videoURL = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnailURL = ((String) in.readValue((String.class.getClassLoader())));
        this.stepId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.recipeId = ((Integer) in.readValue((Integer.class.getClassLoader())));


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(id);
        parcel.writeValue(shortDescription);
        parcel.writeValue(description);
        parcel.writeValue(videoURL);
        parcel.writeValue(thumbnailURL);
        parcel.writeValue(stepId);
        parcel.writeValue(recipeId);
    }

    public String getFormattedDescription() {
        return description.replaceFirst(PATTERN, "");
    }


}

