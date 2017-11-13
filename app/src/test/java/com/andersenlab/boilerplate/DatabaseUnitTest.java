package com.andersenlab.boilerplate;

import android.app.Application;

import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.model.db.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Local test class for testing SQLite database methods.
 */
//@RunWith can be replaced by MockitoAnnotations.initMocks(this); in @Before-method
@RunWith(RobolectricTestRunner.class)
// To use Robolectric you'll need to setup some constants.
// Change it according to your needs.
@Config(application = EmptyApplication.class, sdk=26, packageName = "com.andersenlab.boilerplate",
        manifest="src/main/AndroidManifest.xml")
public class DatabaseUnitTest {

    private Application mockContext;
    private DatabaseHelper dbHelper;

    @Before
    public void init() {
        //MockitoAnnotations.initMocks(this);

        mockContext = RuntimeEnvironment.application;

        dbHelper = DatabaseHelper.getInstance(mockContext);
        clearDb();
    }

    @Test
    public void addImageToDb() {
        clearDb();

        String imageUrl = getImageUrl();

        if (imageUrl != null) {
            long imageId = dbHelper.addImage(new Image(null, imageUrl));

            Image image = dbHelper.getImage(imageId);
            assertTrue("Adding to db failed", image != null && imageUrl.equals(image.getImageUrl()));
        }
    }

    @Test
    public void deleteImagesFromDb() {
        dbHelper.deleteAllImages();

        List<Image> images = dbHelper.getAllImages();
        assertTrue("Cleaning db failed", images.isEmpty());
    }

    @Test
    public void getAllImagesFromDb() {
        clearDb();

        String[] images = mockContext.getResources().getStringArray(R.array.image_items);

        for (String image : images) {
            dbHelper.addImage(new Image(null, image));
        }

        assertTrue("Getting images from db failed", dbHelper.getAllImages().size() == images.length);
    }

    @After
    public void reset() {
        clearDb();
    }

    private String getImageUrl() {
        String[] images = mockContext.getResources().getStringArray(R.array.image_items);
        for (String image : images) {
            if (image != null) {
                return image;
            }
        }
        return null;
    }

    private void clearDb() {
        dbHelper.deleteAllImages();
    }
}
