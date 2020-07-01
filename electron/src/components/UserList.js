import '../css/UserList.css';
import React from 'react';
import CardContent from '@material-ui/core/CardContent';
import Card from '@material-ui/core/Card';
import Divider from '@material-ui/core/Divider';
import User from './User';

function UserList(props) {

	let userDivs = props.users.map(user => {
		return (
			<>
				<User username={user.username} email={user.email} />
				<Divider />
			</>
		);
	});

	return (
		<Card>
			<CardContent>
				{userDivs}
			</CardContent>
		</Card>
	);

}

export default UserList;
