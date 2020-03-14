import React from 'react';
import logo from './logo.svg';
import './App.css';
import spoken from 'spoken';




function App() {
  return (
    <div className="App">
      <button onClick={speak}>
        Press to manually speak
      </button>
      <button onClick={spoken.listen.stop}>
        Stop
      </button>
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
    
  );
}

function speak() {
  spoken.say('Should I turn the hallway light on?').then( speech => {
    spoken.listen().then( transcript => console.log("Answer: " + transcript) )
    /*spoken.say('Sure turn the hallway light on!')*/
  })
}


 export default App;
