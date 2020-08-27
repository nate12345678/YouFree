import React from 'react';
import {
	Button,
	Popper
} from '@material-ui/core';

export default function IntervalPopper(props) {

	return (
		<Popper open={true}>
			<div>Example</div>
			<Button>Edit</Button>
			<Button>Delete</Button>
		</Popper>
	);
}
