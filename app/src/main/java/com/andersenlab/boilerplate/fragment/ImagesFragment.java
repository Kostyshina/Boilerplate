package com.andersenlab.boilerplate.fragment;

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

    public enum LoadingRepository {
        LOAD_FROM_REALM, LOAD_FROM_DB, LOAD_FROM_NETWORK
    }

    public static ImagesFragment newInstance(LoadingRepository loadingRepository) {
        ImagesFragment fragment = new ImagesFragment();
        Bundle args = new Bundle();
        args.putString(LOADING_REPOSITORY_BUNDLE_KEY, loadingRepository.name());
        fragment.setArguments(args);
        return fragment;
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
            }
        } else Timber.e("You must initialize presenter first");
    }

    @Override
    public void showNewImage(Image item) {
        addItemToList(item);
        Timber.i("showNewItem %d", item.getId());
    }

    @Override
    public void showNoNewImages() {
        Toast.makeText(getActivity(), R.string.main_no_items, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(MvpViewException exc) {
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
}
