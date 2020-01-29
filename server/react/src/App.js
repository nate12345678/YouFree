import React from 'react';
import Login from "./Login";
import ScheduleView from "./ScheduleView";

class App extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			login: <Login/>,
			loggedIn: false, //true if successfully logged in
			dummySchedule: [
				[[1, 4], [6, 8], [9, 24]],
				[[0, 3], [6, 8], [9, 24]],
				[[1, 4], [6, 8], [9, 14], [16, 17]],
				[[1, 4], [6, 8], [9, 24]],
				[[1, 4], [6, 8], [9, 24]],
				[[1, 4], [6, 8], [9, 24]],
				[[1, 4], [6, 8], [9, 24]],

			]
		}
	}

	render() {
		let content = <Login/>
		if (this.state.loggedIn) {
			content = <ScheduleView intervals={this.state.dummySchedule} />
		}
		return(
			<div>
				{content}
			</div>
		);
	}
}

export default App;
