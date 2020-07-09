import React from 'react';
import youfree from '../../api/Youfree';
import User from './User';

class Friends extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			friends: []
		};
	}


	componentDidMount() {
		this.getFriends();
	}


	getFriends = async () => {
		try {
			const getFriendsResponse = await youfree.getFriends(this.props.token);

			this.setState({
				friends: getFriendsResponse.data
			});
		} catch (error) {
			this.props.handleError(error);
		}
	}


	render() {
		const userDivs = this.state.friends.map(user => {
			return (
				<li className="users-li" key={user.id}>
					<User username={user.username} email={user.email} />
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
