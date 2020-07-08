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
			const getPendingResponse = await youfree.get('/friends/pending', {
				headers: {
					token: this.props.token
				}
			});

			this.setState({
				pending: getPendingResponse.data
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
		const userDivs = this.state.pending.map(user => {
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

export default Pending;
