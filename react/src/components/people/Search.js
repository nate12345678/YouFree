import React from 'react';
import TextField from '@material-ui/core/TextField';
import User from './User';

class Search extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			query: '',
			// results: []
		};
	}


	handleOnChange = async (event) => {
		const query = event.target.value;

		this.setState({
			query: query
		});

		await this.props.searchUsers(query);
	}


	// onAddFriend = (index) => () => {
	// 	const results = [...this.state.results];
	// 	const result = { ...results[index] }
	// 	switch (result.relationship) {
	// 		case 'PENDING':
	// 			result.relationship = 'FRIENDS';
	// 			break;
	// 		case 'NONE':
	// 			result.relationship = 'SENT';
	// 			break;
	// 		default:
	// 			console.error(`Should not be able to add friend with state ${result.relationship}`);
	// 			break;
	// 	}
	//
	// 	results[index] = result;
	// 	this.setState({
	// 		results: results
	// 	});
	//
	// 	this.props.addFriend(result.user);
	// }
	//
	//
	// onDeleteFriend = (index) => () => {
	// 	const results = [...this.state.results];
	// 	const result = { ...results[index] }
	// 	switch (result.relationship) {
	// 		case 'FRIENDS': // Fallthrough
	// 		case 'SENT': // Fallthrough
	// 		case 'PENDING':
	// 			result.relationship = 'NONE';
	// 			break;
	// 		default:
	// 			console.error(`Should not be able to remove friend with state ${result.relationship}`);
	// 			break;
	// 	}
	//
	// 	results[index] = result;
	// 	this.setState({
	// 		results: results
	// 	});
	//
	// 	this.props.deleteFriend(result.user.id);
	// }


	componentDidMount() {
		this.props.searchUsers('');
	}


	render() {

		const userDivs = this.props.searchResults.map(result => {
			return (
				<li className="users-li" key={result.id}>
					<User
						variant={result.relationship.toLowerCase()}
						username={result.username}
						email={result.email}
						addFriend={() => this.props.addFriend(result)}
						deleteFriend={() => this.props.deleteFriend(result)}
					/>
				</li>
			);
		});

		return (
			<div id="search-card">
				<TextField
					id="search-field"
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

export default Search;
