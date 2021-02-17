import './Markers.css';
import React from 'react';

const timeLabels = [
	{ label: '0:00', offset: -0.75 },
	{ label: '2:00', offset: -0.65 },
	{ label: '4:00', offset: -0.68 },
	{ label: '6:00', offset: -0.65 },
	{ label: '8:00', offset: -0.68 },
	{ label: '10:00', offset: -1.25 },
	{ label: '12:00', offset: -1.20 },
	{ label: '14:00', offset: -1.25 },
	{ label: '16:00', offset: -1.20 },
	{ label: '18:00', offset: -1.25 },
	{ label: '20:00', offset: -1.20 },
	{ label: '22:00', offset: -1.25 },
	{ label: '24:00', offset: -1.25 }
];

const percentLabels = [
	{ label: '0%', offset: -0.25 },
	{ label: '20%', offset: -0.55 },
	{ label: '40%', offset: -0.55 },
	{ label: '60%', offset: -0.55 },
	{ label: '80%', offset: -0.55 },
	{ label: '100%', offset: -0.80 }
];

export default function Markers(props) {

	// Default direction to horizontal
	let offsetDirection = 'left'
	let direction = 'vertical';
	if (props.direction && props.direction === 'horizontal') {
		offsetDirection = 'bottom';
		direction = 'horizontal';
	}

	let labels;
	switch (props.variant) {
		case 'time':
			labels = timeLabels;
			break;
		case 'percent':
			labels = percentLabels;
			break;
		default:
			labels = timeLabels;
	}

	let markers = [];

	// Insert left label
	markers.push(
		<span key="label0"
		      className={`label label-${direction}`}
		      style={{ [offsetDirection]: labels[0].offset + 'em' }}>
			{labels[0].label}
		</span>
	);

	// Insert middle lines + labels
	for (let i = 1; i < labels.length - 1; i++) {
		const left = (i * 100 / (labels.length - 1));
		markers.push(
			<React.Fragment key={labels[i].label}>
				<span className={`label label-${direction}`}
				      style={{ [offsetDirection]: `calc(${labels[i].offset}em + ${left}%)` }}>
					{labels[i].label}
				</span>
				<div className={`marker marker-${direction}`}
				     style={{ [offsetDirection]: left + '%' }}
				/>
			</React.Fragment>
		);
	}

	// Insert right label
	const lastLabel = labels[labels.length - 1];
	markers.push(
		<span key={lastLabel.label}
		      className={`label label-${direction}`}
		      style={{ [offsetDirection]: `calc(${lastLabel.offset}em + 100%)` }}>
			{lastLabel.label}
		</span>);

	return <>{markers}</>;
}
