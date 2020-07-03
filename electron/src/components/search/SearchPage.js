import '../../css/search/SearchPage.css';
import React from 'react';
import youfree from '../../api/Youfree';
import UserList from './UserList';

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
		return (
			<>
				<input onChange={this.handleOnChange} onBlur={this.searchUsers} />
				<UserList users={this.state.users} />
			</>
		);
	}

}

export default SearchPage;
