import React from 'react';
import { Router, Route, Switch, Redirect } from 'react-router-dom';
import { history } from '@core/helpers';
import { useSelector, useDispatch } from '@store/store';
import { alertActions } from '@store/actions';
import { AuthGuardRoute } from '@core/components';
import { HomePage } from '@view/HomePage';
import { LoginPage } from '@view/LoginPage';
import { RegisterPage } from '@view/RegisterPage';

export const App = () => {
    const alert = useSelector(state => state.alert);
    const dispatch = useDispatch();

    history.listen(() => {
        // clear alert on location change
        dispatch(alertActions.clear());
    });

    return (
        <div className="jumbotron">
            <div className="container">
                <div className="col-sm-8 col-sm-offset-2">
                    {alert.message &&
                        <div className={`alert ${alert.type}`}>{alert.message}</div>
                    }
                    <Router history={history}>
                        <Switch>
                            <AuthGuardRoute exact path="/" component={HomePage} />
                            <Route path="/login" component={LoginPage} />

                            <Redirect from="*" to="/" />
                        </Switch>
                    </Router>
                </div>
            </div>
        </div>
    )
}