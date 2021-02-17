import './index.css';
import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { store } from './state/Store';
import App from './components/App';

// Initialize theme
const theme = localStorage.getItem('theme');
document.body.classList.add(theme ? theme : 'light');

// Prevents white flash when initializing dark theme
setTimeout(() => {
	document.body.classList.add('body-transition');
}, 100);

ReactDOM.render(
	// <React.StrictMode>
	// 	<App/>
	// </React.StrictMode>,
	// document.getElementById('root')

	<Provider store={store}>
		<App />
	</Provider>,
	document.querySelector('#root')
);
