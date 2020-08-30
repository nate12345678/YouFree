import React from 'react';
import TextField from '@material-ui/core/TextField';
import youfree from '../../api/Youfree';
import User from './User';

class SearchPage extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			query: '',
			results: []
		};
	}


	searchUsers = async (query) => {
		try {
			const searchUsersResponse = await youfree.searchUsers(this.props.token, query);
			this.setState({
				results: searchUsersResponse.data
			});
		} catch (error) {
			this.props.handleError(error);
		}
	}


	handleOnChange = async (event) => {
		const query = event.target.value;

		this.setState({
			query: query
		});

		await this.searchUsers(query);
	}


	onAddFriend = (index) => () => {
		const results = [...this.state.results];
		const result = { ...results[index] }
		switch (result.relationship) {
			case 'pending':
				result.relationship = 'friends';
				break;
			case 'none':
				result.relationship = 'sent';
				break;
		}

		results[index] = result;
		this.setState({
			results: results
		});

		this.props.addFriend(result.user.id);
	}


	componentDidMount() {
		this.searchUsers('');
	}


	render() {

		const userDivs = this.state.results.map((result, index) => {
			return (
				<li className="users-li" key={result.user.id}>
					<User variant={result.relationship}
					      username={result.user.username}
					      email={result.user.email}
					      addFriend={this.onAddFriend(index)}
					/>
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
