import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';

import { store ,context} from '@store/store';
import { App } from '@view/App';

render(
    <Provider context={context} store={store}>
        <App />
    </Provider>,
    document.getElementById('app')
);