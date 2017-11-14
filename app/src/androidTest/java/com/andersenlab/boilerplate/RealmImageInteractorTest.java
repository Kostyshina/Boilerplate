package com.andersenlab.boilerplate;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.model.realm.RealmInteractor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Instrumental test class for testing work of {@link RealmInteractor} manager on Image objects.
 */

public class RealmImageInteractorTest {

    private Context context;
    private RealmInteractor realmInteractor;

    @Before
    public void init() {
        context = InstrumentationRegistry.getTargetContext();

        Realm.init(context);
        RealmConfiguration testConfig =
                new RealmConfiguration.Builder()
                        .inMemory()
                        .name("test-image.realm").build();

        realmInteractor = new RealmInteractor(Realm.getInstance(testConfig));
        cleanRealm();
    }

    @Test
    public void addImageToRealm_shouldBeAdded() {
        cleanRealm();

        String imageUrl = getImageUrl();

        assertNotNull("All string urls from resources are null", imageUrl);

        Image image = new Image(null, imageUrl);
        image.setId(1);
        realmInteractor.addObject(image);

        Image imageFromRealm = realmInteractor.getObjectById(Image.class, 1);
        assertNotNull("Adding to realm failed", imageFromRealm);
        assertThat("Adding to realm failed",
                imageFromRealm.getContentDescription(), is(image.getContentDescription()));
        assertThat("Adding to realm failed",
                imageFromRealm.getImageUrl(), is(image.getImageUrl()));
    }

    @Test
    public void deleteImagesFromRealm_shouldDeleteAllImages() {
        // Adding at least one item
        String imageUrl = getImageUrl();
        Image image = new Image(null, imageUrl);
        image.setId(1);
        realmInteractor.addObject(image);

        realmInteractor.clearAll(Image.class);

        assertFalse("Cleaning realm failed", realmInteractor.hasObjects(Image.class));
    }

    @Test
    public void getAllImagesFromRealm() {
        cleanRealm();

        String[] images = context.getResources().getStringArray(R.array.image_items);

        assertFalse("Realm manager already has Image items",
                realmInteractor.hasObjects(Image.class));

        for (int i = 0; i < images.length; i++) {
            final Image image = new Image(null, images[i]);
            image.setId(i);
            realmInteractor.addObject(image);
        }

        assertTrue(realmInteractor.getObjects(Image.class).size() == images.length);
    }

    @After
    public void reset() {
        cleanRealm();
        realmInteractor.close();
    }

    private String getImageUrl() {
        String[] images = context.getResources().getStringArray(R.array.image_items);
        for (String image : images) {
            if (image != null) {
                return image;
            }
        }
        return null;
    }

    private void cleanRealm() {
        realmInteractor.clearAll(Image.class);
    }
}
