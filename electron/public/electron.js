const { app, BrowserWindow } = require('electron');
const path = require('path');
const url = require('url');

let mainWindow;


function createWindow () {
	const startUrl = `file://${path.join(__dirname, '../build/index.html')}`;
	mainWindow = new BrowserWindow({ width: 800, height: 600 });

	mainWindow.loadURL(startUrl);

	mainWindow.on('closed', () => mainWindow = null);
}


app.on('ready', createWindow);


app.on('window-all-closed', function () {
	if (process.platform !== 'darwin') {
		app.quit();
	}
});


app.on('activate', function () {
	if (mainWindow === null) {
		createWindow();
	}
});
