import '../css/MyProfilePage.css';
import React from 'react';
import {
	Button,
	Card,
	CardActions,
	CardContent,
	Collapse,
	Dialog,
	DialogActions,
	DialogContent,
	DialogContentText,
	DialogTitle,
	FormControlLabel,
	Icon,
	IconButton,
	Typography
} from '@material-ui/core';
import WeeklySchedule from './WeeklySchedule';
import EditScheduleForm from './common/EditScheduleForm';
import Checkbox from '@material-ui/core/Checkbox';
import {
	addInterval,
	deleteInterval,
	fetchMySchedule,
	updateInterval
} from '../state/Effects';
import { connect } from 'react-redux';


function select(state) {
	return {
		user: state.self,
		schedule: state.mySchedule
	};
}


function mapDispatchToProps(dispatch) {
	return {
		getSchedule: () => dispatch(fetchMySchedule()),
		addInterval: (dayOfWeek, startMin, endMin) => dispatch(addInterval(dayOfWeek, startMin, endMin)),
		updateInterval: (intervalId, dayOfWeek, startMin, endMin) => dispatch(updateInterval(intervalId, dayOfWeek, startMin, endMin)),
		deleteInterval: (intervalId) => dispatch(deleteInterval(intervalId))
	};
}


class ConnectedMyProfilePage extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			editMode: false,
			selectedInterval: null,
			dialogOpen: false,
			dialogSuccessCallback: null,
			askOnOverlap: true
		};
	}


	componentDidMount() {
		this.props.getSchedule();
		const askOnOverlap = localStorage.getItem('ask_on_overlap');
		this.setState({
			askOnOverlap: askOnOverlap === null || askOnOverlap === 'true'
		});
	}


	handleEditClick = () => {
		this.setState((state) => ({
			editMode: !state.editMode,
			selectedInterval: null
		}));
	};


	onEditCancel = () => {
		this.setState({
			editMode: false,
			selectedInterval: null
		});
	}


	onIntervalSelection = (interval) => {
		this.setState({
			editMode: true,
			selectedInterval: interval
		});
	}


	onIntervalDeselection = () => this.onIntervalSelection(null);


	hasOverlap = (dayOfWeek, startMin, endMin) => {
		let day = this.props.schedule[dayOfWeek];
		if (this.state.selectedInterval) {
			day = day.filter(interval => interval.id !== this.state.selectedInterval.id);
		}

		// Fast-forward to insertion position
		let next = 0;
		while (next < day.length) {
			if (startMin <= day[next].startMin) break;
			next++;
		}

		// Check if previous or next intervals overlap
		return (
			(next > 0 && day[next - 1].endMin >= startMin)
			|| (next < day.length && day[next].startMin <= endMin)
		);
	}


	addInterval = (dayOfWeek, startMin, endMin) => {
		if (this.state.askOnOverlap && this.hasOverlap(dayOfWeek, startMin, endMin)) {
			this.setState({
				dialogOpen: true,
				dialogSuccessCallback: () => this.props.addInterval(dayOfWeek, startMin, endMin)
			});
			return;
		}

		this.props.addInterval(dayOfWeek, startMin, endMin);
	}


	updateInterval = (dayOfWeek, startMin, endMin) => {
		if (this.state.askOnOverlap && this.hasOverlap(dayOfWeek, startMin, endMin)) {
			this.setState({
				dialogOpen: true,
				dialogSuccessCallback: () => this.props.updateInterval(this.state.selectedInterval.id, dayOfWeek, startMin, endMin)
			});
			return;
		}

		this.props.updateInterval(this.state.selectedInterval.id, dayOfWeek, startMin, endMin);
		this.setState({
			selectedInterval: null
		});
	}


	deleteInterval = () => {
		this.props.deleteInterval(this.state.selectedInterval.id);

		this.setState({
			selectedInterval: null
		});
	}


	handleDialogClose = () => {
		this.setState({
			dialogOpen: false,
			dialogSuccessCallback: null
		});
	}


	handleDialogSuccess = (askOnOverlap) => () => {
		this.state.dialogSuccessCallback();
		this.setState({
			dialogOpen: false,
			dialogSuccessCallback: null,
			selectedInterval: null,
			askOnOverlap: askOnOverlap
		});
		localStorage.setItem('ask_on_overlap', askOnOverlap);
	}


	render() {
		return (
			<>
				<Card className="my-profile-card" elevation={4}>
					<CardContent>
						<div className="my-profile-user">
							<div className="my-profile-pic"/>
							<div className="my-profile-info">
								<Typography className="my-profile-username" variant="h4">{this.props.user.username}</Typography>
								<Typography className="my-profile-email" variant="h6">{this.props.user.email}</Typography>
							</div>
						</div>
					</CardContent>
				</Card>
				<Card className="my-weekly-schedule-card" elevation={4}>
					<CardContent>
						<WeeklySchedule schedule={this.props.schedule}
						                onIntervalSelection={this.onIntervalSelection}
						                selectedInterval={this.state.selectedInterval}
						/>
					</CardContent>
					<CardActions>
						<IconButton id="edit-button"
						            aria-expanded={this.state.editMode}
						            aria-label="edit"
						            onClick={this.handleEditClick}>
							<Icon id="edit-icon">{this.state.editMode ? 'cancel' : 'edit'}</Icon>
						</IconButton>
					</CardActions>
					<Collapse in={this.state.editMode} unmountOnExit>
						<EditScheduleForm interval={this.state.selectedInterval}
						                  onSubmit={this.addInterval}
						                  onUpdate={this.updateInterval}
						                  onDelete={this.deleteInterval}
						                  onCancel={this.onEditCancel}
						                  onIntervalDeselection={this.onIntervalDeselection}
						/>
					</Collapse>
				</Card>
				<OverlapDialog open={this.state.dialogOpen}
				               onClose={this.handleDialogClose}
				               onSuccess={this.handleDialogSuccess}
				/>
			</>
		);
	}
}


function OverlapDialog({ open, onClose, onSuccess }) {
	let [askOnOverlap, setAskOnOverlap] = React.useState(true);
	let handleCheckboxChange = (event) => setAskOnOverlap(event.target.checked);

	return (
		<Dialog open={open} onEnter={() => setAskOnOverlap(true)} onClose={onClose}>
			<DialogTitle>Combine time intervals?</DialogTitle>
			<DialogContent>
				<DialogContentText>
					The time you entered overlaps with an existing interval.
					Would you like to combine the intervals?
				</DialogContentText>
				<FormControlLabel
					name="askOnOverlap"
					control={
						<Checkbox checked={askOnOverlap}
						          size="small"
						          color="primary"
						          onChange={handleCheckboxChange}
						/>
					}
					label="Ask me this every time"
					labelPlacement="start"
					style={{width: '100%', paddingLeft: 'auto', color: 'rgba(0, 0, 0, 0.54)'}}
				/>
			</DialogContent>
			<DialogActions>
				<Button autoFocus color="primary" onClick={onClose}>Cancel</Button>
				<Button color="primary" onClick={onSuccess(askOnOverlap)}>Yes</Button>
			</DialogActions>
		</Dialog>
	);
}

const MyProfilePage = connect(select, mapDispatchToProps)(ConnectedMyProfilePage);
export default MyProfilePage;
