import '../../css/search/SearchPage.css';
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
			const searchUsersResponse = await youfree.get('/search', {
				headers: {
					token: this.props.token,
					query: this.state.query
				}
			});

			this.setState({
				users: searchUsersResponse.data
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


	handleOnChange = (event) => {
		this.setState({
			query: event.target.value
		});
	}


	render() {

		const userDivs = this.state.users.map(user => {
			return (
				<li className="users-li" key={user.id}>
					<User username={user.username} email={user.email} addFriend={() => this.props.addFriend(user.id)} />
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
