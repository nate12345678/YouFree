import '../css/MyScheduleCard.css';
import React from 'react';
import EditScheduleForm from './EditScheduleForm';
import DailySchedule from './DailySchedule';
import {
	Card,
	CardActions,
	CardContent,
	Collapse,
	Icon,
	IconButton,
	Typography
} from '@material-ui/core';

class MyScheduleCard extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			isExpanded: false
		}
	}


	handleExpansion = () => {
		this.setState((prevState) => ({
			isExpanded: !prevState.isExpanded
		}));
	}


	onEditCancel = () => {
		this.setState({
			isExpanded: false
		});
	}


	render() {
		const schedule = this.props.schedule
			? <DailySchedule schedule={this.props.schedule} />
			: 'Loading...';

		return (
			<Card className="my-daily-schedule-card" elevation={4}>
				<CardContent style={{ width: '100%' }}>
					<Typography className="title" variant="h6">My Schedule</Typography>
					{schedule}
				</CardContent>
				<CardActions>
					<IconButton id="card-expand-button"
					            aria-expanded={this.state.isExpanded}
					            aria-label="show more"
					            onClick={this.handleExpansion}>
						<Icon id="card-expand-icon" className={this.state.isExpanded ? 'expanded' : 'collapsed'}>expand_more</Icon>
					</IconButton>
				</CardActions>
				<Collapse className="edit-daily-schedule-collapse" in={this.state.isExpanded} unmountOnExit>
					<div className="edit-daily-schedule-form">
						<EditScheduleForm onSubmit={this.props.onAddInterval} onCancel={this.onEditCancel} />
					</div>
				</Collapse>
			</Card>
		);
	}

}

export default MyScheduleCard;
