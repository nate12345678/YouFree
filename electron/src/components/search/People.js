import React from 'react';
import {
	Card,
	CardContent,
	Paper,
	Tab,
	Tabs
} from '@material-ui/core';
import SearchPage from './SearchPage';

class People extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			currentTab: 0
		}
	}


	handleChange = (event, newValue) => {
		this.setState(() => {
			return {
				currentTab: newValue
			}
		});
	}


	render() {

		let content;
		switch (this.state.currentTab) {
			case 0:
				content = <div>Friends</div>
				break;
			case 1:
				content = <div>Pending</div>
				break;
			default:
				content = <SearchPage token={this.props.token} addFriend={this.props.addFriend} />
		}

		return (
			<Card id="peopleCard" elevation={4}>
				<Paper id="tabs" square elevation={1}>
					<Tabs value={this.state.currentTab}
					      variant="fullWidth"
					      indicatorColor="primary"
					      textColor="primary"
					      onChange={this.handleChange}>

						<Tab label="Friends" />
						<Tab label="Pending" />
						<Tab label="Search" />
					</Tabs>
				</Paper>

				<CardContent id="peopleCardContent">
					{content}
				</CardContent>
			</Card>
		);
	}
}

export default People;
