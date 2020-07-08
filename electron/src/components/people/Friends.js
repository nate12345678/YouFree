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
			const getFriendsResponse = await youfree.get('/friends', {
				headers: {
					token: this.props.token
				}
			});

			this.setState({
				friends: getFriendsResponse.data
			});
		} catch (error) {
			if (error.response !== undefined) {
				console.log(error.response);
				// TODO: pop up with error message
				return;
			}

			console.log('An unknown error has occurred');
			// TODO: pop up with error message
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
