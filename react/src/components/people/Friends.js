import React from 'react';
import User from './User';
import { Icon } from '@material-ui/core';

class Friends extends React.Component {

	componentDidMount() {
		this.props.getFriends();
	}


	render() {
		if (this.props.friends.length === 0) {
			return (
				<React.Fragment>
					<div className="filler-content">
						<Icon className="filler-icon" color="inherit" style={{ paddingRight: '0.25em' }}>person_add</Icon>
						<div>Your friends list is empty.</div>
						<div>Try searching for your friends!</div>
					</div>
				</React.Fragment>
			);
		}

		const userDivs = this.props.friends.map(user => {
			return (
				<li className="users-li" key={user.id}>
					<User variant="friends" username={user.username} email={user.email}/>
				</li>
			);
		});

		return (
			<ul className="users-ul">
				{userDivs}
			</ul>
		);
	}

}

export default Friends;
