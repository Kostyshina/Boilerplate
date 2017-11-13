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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumental test class for testing work of {@link Realm} manager.
 */

public class RealmInstrumentalTest {

    private Context context;
    private RealmInteractor realmInteractor;

    @Before
    public void init() {
        context = InstrumentationRegistry.getTargetContext();

        Realm.init(context);
        RealmConfiguration testConfig =
                new RealmConfiguration.Builder()
                        .inMemory()
                        .name("test-realm").build();

        realmInteractor = new RealmInteractor(Realm.getInstance(testConfig));
        cleanRealm();
    }

    @Test
    public void addImageToRealm() {
        cleanRealm();

        String imageUrl = getImageUrl();

        if (imageUrl != null) {
            Image image = new Image(null, imageUrl);
            image.setId(1);
            realmInteractor.addObject(image);

            Image imageFromRealm = realmInteractor.getObjectById(Image.class, 1);
            assertTrue("Adding to realm failed", imageFromRealm != null &&
                    imageUrl.equals(imageFromRealm.getImageUrl()));
        }
    }

    @Test
    public void deleteImagesFromRealm() {
        realmInteractor.clearAll(Image.class);

        assertFalse("Cleaning realm failed", realmInteractor.hasObjects(Image.class));
    }

    @Test
    public void getAllImagesFromRealm() {
        cleanRealm();

        String[] images = context.getResources().getStringArray(R.array.image_items);

        if (!realmInteractor.hasObjects(Image.class)) {
            int i = 0;
            for (String imageUrl : images) {
                i++;
                final Image image = new Image(null, imageUrl);
                image.setId(i);
                realmInteractor.addObject(image);
            }
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
