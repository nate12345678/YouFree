import React from 'react';
import User from './User';
import youfree from '../../api/Youfree';

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
	}


	render() {
		const userDivs = this.state.pending.map(user => {
			return (
				<li className="users-li" key={user.id}>
					<User variant="pending" username={user.username} email={user.email} addFriend={() => this.props.addFriend(user.id)} deleteFriend={() => this.props.deleteFriend(user.id)} />
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
