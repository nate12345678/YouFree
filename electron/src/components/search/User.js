import '../../css/search/User.css';
import React from 'react'

function User(props) {

	return (
		<div className="user-content">
			<div className="user-pic"> </div>
			<div className="user-info">
				<span className="user-username">{props.username}</span>
				<span className="user-email">{props.email}</span>
			</div>
		</div>
	);

}

export default User;
