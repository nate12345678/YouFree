import React from 'react';
import User from './User';
import youfree from '../../api/Youfree';
import { Icon } from '@material-ui/core';

class Pending extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			pending: []
		};
	}


	componentDidMount() {
		this.getPending();
	}


	getPending = async () => {
		try {
			const getPendingResponse = await youfree.getPending(this.props.token);

			this.setState({
				pending: getPendingResponse.data
			});
		} catch (error) {
			this.props.handleError(error);
		}
	};


	addFriend = (userId) => () => {
		const pending = this.state.pending.filter(user => user.id !== userId);
		this.setState({
			pending: pending
		});
		this.props.addFriend(userId);
		// TODO: Should show loading until this succeeds
	};


	deleteFriend = (userId) => () => {
		const pending = this.state.pending.filter(user => user.id !== userId);
		this.setState({
			pending: pending
		});
		this.props.deleteFriend(userId);
		// TODO: Should show loading until this succeeds
	};


	render() {
		if (this.state.pending.length === 0) {
			return (
				<React.Fragment>
					<div className="filler-content">
						<Icon className="filler-icon" color="inherit">check_circle_outline</Icon>
						<div>You have no pending friend requests!</div>
					</div>
				</React.Fragment>
			);
		}


		const userDivs = this.state.pending.map(user => {
			return (
				<li className="users-li" key={user.id}>
					<User variant="pending"
					      username={user.username}
					      email={user.email}
					      addFriend={this.addFriend(user.id)}
					      deleteFriend={this.deleteFriend(user.id)}/>
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
