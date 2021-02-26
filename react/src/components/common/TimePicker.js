import './TimePicker.css';
import React from 'react';
import {
	Button,
	List,
	ListItem,
	Popover,
	TextField,
	Typography
} from '@material-ui/core';

export default function TimePicker(props) {

	const [anchorEl, setAnchorEl] = React.useState(null);
	const [selectedHour, setSelectedHour] = React.useState(12);
	const [selectedMin, setSelectedMin] = React.useState('00');
	const [selectedHalf, setSelectedHalf] = React.useState('AM');

	const handleClick = (event) => {
		setAnchorEl(event.target);
	}

	const handleClose = () => {
		setAnchorEl(null);
	}

	let hours = [];
	for (let i = 1; i <= 12; i++) {
		hours.push(
			<ListItem button key={i} selected={i === selectedHour} onClick={() => setSelectedHour(i)}>
				<span className="time-picker-entry">{i}</span>
			</ListItem>
		);
	}

	let mins = [];
	for (let i = 0; i < 10; i++) {
		const min = `0${i}`;
		mins.push(
			<ListItem button key={i} selected={min === selectedMin} onClick={() => setSelectedMin(min)}>
				<span className="time-picker-entry">{min}</span>
			</ListItem>
		);
	}

	for (let i = 10; i <= 59; i++) {
		const min = `${i}`;
		mins.push(
			<ListItem button key={i} selected={min === selectedMin} onClick={() => setSelectedMin(min)}>
				<span className="time-picker-entry">{min}</span>
			</ListItem>
		);
	}

	return (
		<div>
			<span className="time-picker-button">
				<TextField variant="outlined"
				           size="medium"
				           margin="normal"
				           label={props.label}
				           style={{zIndex: 1}}
				           value={selectedHour + ':' + selectedMin + ' ' + selectedHalf}
				/>
				<div className="time-picker-button-hack" onClick={handleClick}/>
			</span>
			<Popover
				open={anchorEl != null}
				anchorEl={anchorEl}
				onClose={handleClose}
				anchorOrigin={{
					vertical: 'top',
					horizontal: 'center'
				}}
				transformOrigin={{
					vertical: 'top',
					horizontal: 'center'
				}}
				style={{width: '300px'}}
			>
				<div style={{display: 'flex', flexDirection: 'column'}}>
					<Typography className="time-picker-title" variant="h6">Start Time</Typography>
					<div className="time-picker">
						<List dense className="time-picker-col">{hours}</List>
						<List dense className="time-picker-col time-picker-col-minutes">{mins}</List>
						<List dense className="time-picker-col">
							<ListItem button selected={selectedHalf === 'AM'} onClick={() => setSelectedHalf('AM')}>
								<span className="time-picker-entry">AM</span>
							</ListItem>
							<ListItem button selected={selectedHalf === 'PM'} onClick={() => setSelectedHalf('PM')}>
								<span className="time-picker-entry">PM</span>
							</ListItem>
						</List>
					</div>
				</div>
			</Popover>
		</div>
	);
}
