# React

## DOM Manipulation
- Document Object Model (tree representation of HTML) is the process of dynamically updating the structure, content, or styling of a webpage using JavaScript. The browser represents HTML as a tree structure called the DOM, and JavaScript interacts with this tree to modify elements, handle events, and update the UI without reloading the page.
- Direct DOM manipulation can become complex and inefficient for large applications, which is why frameworks like React abstract it using a virtual DOM
### Core Operations
- Selecting Elements
```js
document.getElementById("id");
document.getElementsByClassName("class");
document.getElementsByTagName("div");
document.querySelector("#id");        // modern
document.querySelectorAll(".class");  // modern
```
- Changing Content
```js
element.textContent = "New Text";
element.innerHTML = "<b>Bold</b>";
```
- Changing Styles
```js
element.style.color = "red";
element.style.backgroundColor = "black";
```
- Adding/Removing Element
```js
const newDiv = document.createElement("div");
newDiv.textContent = "New Element";

document.body.appendChild(newDiv);


//remove
element.remove();
```
- Event Handling

```js
button.addEventListener("click", () => {
    console.log("Clicked!");
});
```
### Virtual DOM
- The Virtual DOM is a lightweight JavaScript representation of the real DOM. When the state of a React component changes, React creates a new Virtual DOM tree and compares it with the previous one using a diffing algorithm. 
- Based on the differences, it updates only the necessary parts of the real DOM, making the process more efficient than direct DOM manipulation
- Virtual DOM does not replace DOM, Virtual DOM simply optimizes DOM.

## React Component
- A React component is a reusable unit of UI that takes inputs as props and manages internal state. When state or props change, the component re-renders and React updates the DOM efficiently
- It is a function that describes UI based on data (state + props)
 
```js
function App() {
  return <Counter title="Simple Counter" />;
}

function Counter() {
  // STATE: internal data of component
  const [count, setCount] = React.useState(0);

  return (
    <div>
      <h2>{props.title}</h2> {/* PROP */}
      <h1>{count}</h1>      {/* STATE */}

      {/* Event triggers state change */}
      <button onClick={() => setCount(count + 1)}>
        Increment
      </button>
    </div>
  );
}
```
##### State
- State is the internal memory of a component.
```js
const [count, setCount] = React.useState(0);
```
- `count` → current value
- `setCount` → function to update it
- 0 → initial value

##### Props 
- Props are inputs passed from parent to child component `props.title`.

## React Component lifecycle 
- The React component lifecycle consists of mounting, updating, and unmounting phases. In functional components, we use the useEffect hook to handle lifecycle events like data fetching, subscriptions, and cleanup.
- It has 3 stages
    - Mount → Component created
    - Update → State/props change
    - Unmount → Component removed

## React Hook 
- React Hooks are functions that allow functional components to use features like state, lifecycle, and context.
- They were introduced to replace class components and enable better logic reuse through custom hooks
- Before introduction of Hooks, class components could only manage state and use lifecycle methods.
- Examples
    - `useState` - State
    - `useEffect` - lifecycle
    - `useContext` - context
    - `useMemo`, `useCallback` - Performance
    -  `useRef` - Refs(DOM) 
- Developers are also provided with the facility to create and use Custom Hooks.

## `useState` 
- `useState` is a React Hook that allows functional components to manage internal state.
- It returns the current state and a setter function. 
- When the setter is called, React schedules a re-render and updates the UI efficiently using the Virtual DOM
- State updates are asynchronous.
```js

function Counter() {
  // STATE: internal data of component
  const [count, setCount] = React.useState(0);

  return (
    <div>
      <h1>{count}</h1>      {/* STATE */}

      {/* Event triggers state change */}
      <button onClick={() => setCount(count + 1)}>
        Increment
      </button>
    </div>
  );
}
```

```js
const [count, setCount] = React.useState(0);
```
- `count` → current value
- `setCount` → function to update it
- 0 → initial value

## `useEffect`
- `useEffect` is a React Hook used to handle side effects like API calls, subscriptions, and timers.
- It runs after the component renders, and its execution is controlled by a dependency array.
- It can also return a cleanup function to handle resource cleanup when the component unmounts or before the effect re-runs.

```js
useEffect(() => {
  console.log("Effect runs");
}, []);
```
#### Dependency array
##### No Dependency array

```js
useEffect(() => {
  console.log("Runs every render");
});
```
- Runs on every mount and every update.

##### Empty array `[]`

```js
useEffect(() => {
  console.log("Runs once");
}, []);
```
- Runs only on mount.
##### With dependencies

```js
useEffect(() => {
  console.log("Runs when count changes");
}, [count]);
```
- Runs on mount and when `count` changes.

## API Integration
- API Integration in frontend is the process of fetching data from backend services and rendering it in the UI.

```js
function Users() {
  const [users, setUsers] = React.useState([]);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    fetch("/api/users") // API call
      .then(res => res.json())
      .then(data => {
        setUsers(data);     // store data
        setLoading(false);  // update loading state
      })
      .catch(error => {
        console.error(error);
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading...</p>;

  return (
    <ul>
      {users.map(user => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
}
```
- State Management - Stores data and UI State.

```js
const [users, setUsers] = useState([]);
const [loading, setLoading] = useState(true);
```
- Trigger API call - Runs on mount

```js
useEffect(() => {
  fetch("/api/users");
}, []);
```
- Handle Response

```js
.then(res => res.json())
.then(data => setUsers(data));
```
- Update UI

```js
{users.map(user => <li>{user.name}</li>)}
```
