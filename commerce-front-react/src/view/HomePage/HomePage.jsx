import React, { useState ,useEffect} from 'react';
import { Link } from 'react-router-dom';
import UserService from "@core/services/user.service";
import WebsocketService from '@src/core/services/websocket.service'

export const HomePage = () => {

    const [users, setUsers] = useState([]);

    useEffect(() => {
        UserService.getAllUsers()
            .then(data => setUsers(data));

        WebsocketService.wsConnect();
    }, []);

    return (
        <div className="col-md-6 col-md-offset-3">
            <h1>Hi !</h1>
            <p>You are logged in with React!!</p>
            <h3>All registered users:</h3>
            <p>
                <Link to="/login">Logout</Link>
            </p>
            {users &&
                <ul>
                    {users.map((user) =>
                        <li key={user.userId}>
                            {user.userAccount + ' ' + user.userNickName}
                        </li>
                    )}
                </ul>
            }
        </div>
    );

}