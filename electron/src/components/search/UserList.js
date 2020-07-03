import '../../css/search/UserList.css';
import React from 'react';
import CardContent from '@material-ui/core/CardContent';
import Card from '@material-ui/core/Card';
import Divider from '@material-ui/core/Divider';
import User from './User';

function UserList(props) {

	let userDivs = props.users.map(user => {
		return (
			<li className="users-li" key={user.id}>
				<User username={user.username} email={user.email} />
			</li>
		);
	});

	return (
		<Card elevation={4}>
			<CardContent>
				<ul className="users-ul">
					{userDivs}
				</ul>
			</CardContent>
		</Card>
	);

}

export default UserList;
