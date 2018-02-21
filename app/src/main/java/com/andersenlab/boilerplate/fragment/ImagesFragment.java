package com.andersenlab.boilerplate.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andersenlab.boilerplate.R;
import com.andersenlab.boilerplate.activity.MainActivity;
import com.andersenlab.boilerplate.adapter.ImageAdapter;
import com.andersenlab.boilerplate.listener.LoadingFragmentContainer;
import com.andersenlab.boilerplate.listener.LoadingFragmentContainer.LoadingListener;
import com.andersenlab.boilerplate.model.Image;
import com.andersenlab.boilerplate.presenter.ImagePresenter;
import com.andersenlab.boilerplate.view.ImageMvpView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Fragment for loading {@link Image} objects to recyclerView.
 * Attached context must implement {@link com.andersenlab.boilerplate.listener.LoadingFragmentContainer} interface
 * if you want receive loading updates.
 */

public class ImagesFragment extends BaseFragment implements ImageMvpView,
        MainActivity.OnAddItemClickListener {

    private static final String PRESENTER_BUNDLE_KEY = "com.andersenlab.boilerplate.fragment.presenter";
    private static final String IMAGES_BUNDLE_KEY = "com.andersenlab.boilerplate.fragment.imageList";
    private static final String LOADING_REPOSITORY_BUNDLE_KEY = "com.andersenlab.boilerplate.fragment.loadingRepository";

    @BindView(R.id.rv_image_list_items) RecyclerView imageRecyclerView;

    private Unbinder unbinder;

    private ImagePresenter imagePresenter;
    private ImageAdapter imageAdapter;
    private ArrayList<Image> imageList;
    private LoadingRepository imagesRepository;

    private LoadingListener listener;

    public static ImagesFragment newInstance(LoadingRepository loadingRepository) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        args.putString(LOADING_REPOSITORY_BUNDLE_KEY, loadingRepository.name());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoadingFragmentContainer) {
            listener = ((LoadingFragmentContainer) context).onRequestLoadingListener();
        }
        if (listener == null)
            listener = LoadingFragmentContainer.DEFAULT_LISTENER;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String loadingRepositoryName = LoadingRepository.LOAD_FROM_REALM.name();
        if (getArguments() != null) {
            loadingRepositoryName = getArguments()
                    .getString(LOADING_REPOSITORY_BUNDLE_KEY, LoadingRepository.LOAD_FROM_REALM.name());
        }
        imagesRepository = LoadingRepository.valueOf(loadingRepositoryName);

        imageAdapter = new ImageAdapter(Glide.with(this));

        if (savedInstanceState != null) {
            imageList = savedInstanceState.getParcelableArrayList(IMAGES_BUNDLE_KEY);
            imagePresenter = savedInstanceState.getParcelable(PRESENTER_BUNDLE_KEY);
        }

        if (imageList == null)
            imageList = new ArrayList<>();

        if (!imageList.isEmpty()) {
            imageAdapter.setItems(imageList);
        }

        RecyclerViewPreloader<Image> preloader =
                new RecyclerViewPreloader<>(this, imageAdapter,
                        imageAdapter.getPreloadSizeProvider(), 6);
        imageRecyclerView.addOnScrollListener(preloader);
        imageRecyclerView.setItemViewCacheSize(0);

        ((SimpleItemAnimator) imageRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        imageRecyclerView.setAdapter(imageAdapter);

        if (imagePresenter == null)
            imagePresenter = new ImagePresenter();
        imagePresenter.attachView(this);
        listener.onLoadingState(imagePresenter.isLoading());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getContext() != null) {
            DividerItemDecoration divider = new DividerItemDecoration(
                    getContext(),
                    DividerItemDecoration.VERTICAL
            );
            Reference<Drawable> drawableWeakReference =
                    new WeakReference<>(ContextCompat.getDrawable(getContext(), R.drawable.shape_line_divider));
            if (drawableWeakReference.get() != null) {
                divider.setDrawable(drawableWeakReference.get());
            }
            imageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            imageRecyclerView.addItemDecoration(divider);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Timber.i("onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(IMAGES_BUNDLE_KEY, imageList);
        outState.putParcelable(PRESENTER_BUNDLE_KEY, imagePresenter);
    }

    @Override
    public void addItem() {
        Timber.i("addItem type: %s", imagesRepository.toString());
        if (imagePresenter != null) {
            switch (imagesRepository) {
                case LOAD_FROM_DB:
                    imagePresenter.loadItemFromDb(getContext());
                    break;
                case LOAD_FROM_REALM:
                    imagePresenter.loadItemFromRealm();
                    break;
                case LOAD_FROM_NETWORK:
                    imagePresenter.loadItemThroughRetrofit();
                    break;
                default:
                    Timber.e("Requested loading repository is not available");
                    listener.onError();
            }
        } else {
            Timber.e("You must initialize presenter first");
            listener.onError();
        }
    }

    @Override
    public void showNewImage(Image item) {
        addItemToList(item);
        listener.onSuccess();
        Timber.i("showNewItem %d", item.getId());
    }

    @Override
    public void showNoNewImages() {
        listener.onError();
        Toast.makeText(getActivity(), R.string.main_no_items, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(MvpViewException exc) {
        listener.onError();
        Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_LONG).show();
        Timber.e(exc);
    }

    private void addItemToList(Image item) {
        imageList.add(item);
        imageAdapter.addItem(item);
        imageRecyclerView.scrollToPosition(imageList.size() - 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        imagePresenter.detachView();
    }

    public enum LoadingRepository {
        LOAD_FROM_REALM, LOAD_FROM_DB, LOAD_FROM_NETWORK
    }
}
