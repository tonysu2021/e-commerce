import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { useSelector } from '@store/store';
import PropTypes from 'prop-types';

export const AuthGuardRoute = ({ component: Component, ...rest }) => {
    const user = useSelector(state => state.authentication.user);
    return (
        <Route {...rest} render={(props) => (
            user && user.accessToken
                ? <Component {...props} />
                : <Redirect to={{ pathname: '/login', state: { from: props.location } }} />
        )} />
    )
}

AuthGuardRoute.propTypes = {
    component : PropTypes.func,
    location : PropTypes.object
}
