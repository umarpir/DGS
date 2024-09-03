
import './App.css';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Organisation from './pages/organisation';
import Organisations from './pages/organisations';
import Home from './pages/home';
import Personnel from './pages/personnel';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Home/>,
    children: [
      { index: true, element: <Organisations/> }, // Homepage displaying all organisations
      { path: '/:id', element: <Organisation/> },
      { path: 'personnel/:id', element: <Personnel/> } // Single organisation based on ID parameter
      // Uncomment if you have a Personnel component
      // { path: 'organisations/:id/personnel', element: <Personnel /> } // Personnel based on organisation ID parameter
    ]
  }
]);


function App() {
  return (
    <RouterProvider router={router}/>

  );
}

export default App;
