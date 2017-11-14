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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Local test class for testing SQLite database methods for {@link Image} table.
 */

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class, sdk = 26, packageName = "com.andersenlab.boilerplate",
        manifest="app/src/main/AndroidManifest.xml")
public class SQLiteDatabaseImageUnitTest {

    private Application mockContext;
    private DatabaseHelper dbHelper;

    @Before
    public void init() {
        mockContext = RuntimeEnvironment.application;

        dbHelper = DatabaseHelper.getInstance(mockContext);
        clearDb();
    }

    @Test
    public void addImageToDb() {
        clearDb();

        String imageUrl = getImageUrl();

        assertNotNull("All string urls from resources are null", imageUrl);

        long imageId = dbHelper.addImage(new Image(null, imageUrl));

        Image image = dbHelper.getImage(imageId);
        assertTrue("Adding to db failed", image != null && imageUrl.equals(image.getImageUrl()));
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
