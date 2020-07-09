import React from 'react';
import TextField from '@material-ui/core/TextField';
import youfree from '../../api/Youfree';
import User from './User';

class SearchPage extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			query: '',
			users: []
		};
	}


	searchUsers = async () => {
		try {
			const searchUsersResponse = await youfree.searchUsers(this.props.token, this.state.query);

			this.setState({
				users: searchUsersResponse.data
			});

		} catch (error) {
			this.props.handleError(error);
		}
	}


	handleOnChange = (event) => {
		this.setState({
			query: event.target.value
		});
	}


	render() {

		const userDivs = this.state.users.map(user => {
			return (
				<li className="users-li" key={user.id}>
					<User variant="stranger" username={user.username} email={user.email} addFriend={() => this.props.addFriend(user.id)} />
				</li>
			);
		});

		return (
			<div id="search-card">
				<TextField id="search-field"
				           variant="outlined"
				           margin="normal"
				           placeholder="Search"
				           label="Search"
				           onChange={this.handleOnChange}
				           onBlur={this.searchUsers}
				           fullWidth
				/>
				<ul className="users-ul">
					{userDivs}
				</ul>
			</div>
		);
	}

}

export default SearchPage;
