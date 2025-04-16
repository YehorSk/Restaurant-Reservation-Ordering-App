export function handleError(error, store, t = (key) => key) {

    console.error(error);
    if (error.response) {
        if (error.response.status === 422 || error.response.status === 409) {
            store.errors = error.response.data.errors;
            store.credentials = error.response.data.message;
            store.failure = error.response.data.message || t('errors.validation');
        } else {
            store.failure = error.response.data.message || t('errors.unexpected');
        }
    } else {
        store.failure = t('errors.network');
    }
}