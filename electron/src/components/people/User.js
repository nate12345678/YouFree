import '../../css/people/User.css';
import React from 'react'
import {
	Button,
	Icon,
} from '@material-ui/core';

function User(props) {

	return (
		<div className="user-content">
			<div className="user-pic"> </div>
			<div className="user-info">
				<span className="user-username">{props.username}</span>
				<span className="user-email">{props.email}</span>
			</div>
			<Button className="user-add-friend-button"
			        color="primary"
			        variant="outlined"
			        size="small"
			        onClick={props.addFriend}
			        disableElevation>
				<Icon>person_add</Icon>
			</Button>
		</div>
	);

}

export default User;
