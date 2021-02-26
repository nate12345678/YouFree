import React from 'react';
import User from './User';
import { Icon } from '@material-ui/core';

class Pending extends React.Component {

	componentDidMount() {
		this.props.getPending();
		this.props.clearNotifications();
	}


	// addFriend = (userId) => () => {
	// 	const pending = this.state.pending.filter(user => user.id !== userId);
	// 	this.setState({
	// 		pending: pending
	// 	});
	// 	this.props.addFriend(userId);
	// 	// TODO: Should show loading until this succeeds
	// };


	// deleteFriend = (userId) => () => {
	// 	const pending = this.state.pending.filter(user => user.id !== userId);
	// 	this.setState({
	// 		pending: pending
	// 	});
	// 	this.props.deleteFriend(userId);
	// 	// TODO: Should show loading until this succeeds
	// };


	render() {
		if (this.props.pending.length === 0) {
			return (
				<React.Fragment>
					<div className="filler-content">
						<Icon className="filler-icon" color="inherit">check_circle_outline</Icon>
						<div>You have no pending friend requests!</div>
					</div>
				</React.Fragment>
			);
		}


		const userDivs = this.props.pending.map(user => {
			return (
				<li className="users-li" key={user.id}>
					<User variant="pending"
					      username={user.username}
					      email={user.email}
					      addFriend={() => this.props.addFriend(user)}
					      deleteFriend={() => this.props.deleteFriend(user)}
					/>
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

export default Pending;
