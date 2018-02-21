package com.andersenlab.boilerplate.listener;

/**
 * Interface that used to receive some loading updates.
 */

public interface LoadingFragmentContainer {

    LoadingListener DEFAULT_LISTENER = new LoadingListener() {
        @Override
        public void onLoadingState(boolean inProgress) {
            // no need
        }

        @Override
        public void onSuccess() {
            // no need
        }

        @Override
        public void onError() {
            // no need
        }
    };

    LoadingListener onRequestLoadingListener();

    interface LoadingListener {
        void onLoadingState(boolean inProgress);
        void onSuccess();
        void onError();
    }
}
