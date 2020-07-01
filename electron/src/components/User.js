import '../css/User.css';
import React from 'react'

function User(props) {

	return (
		<div>
			<div className="user-pic"></div>
			<div className="user-info-div">
				<span>{props.username}</span>
				<span>{props.email}</span>
			</div>
		</div>
	);

}

export default User;
