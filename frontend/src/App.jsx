import './App.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import Home from "./component/home/Home.jsx";
import {createBrowserRouter, createRoutesFromElements, Route, RouterProvider} from "react-router-dom";
import RootLayout from "./component/layout/RootLayout.jsx";
import VeterinarianListing from "./component/veterinarian/VeterinarianListing.jsx";

function App() {
    const router = createBrowserRouter(
        createRoutesFromElements(
            <Route path="/" element={<RootLayout />}>
                <Route index element={<Home />} />
                <Route path='/doctors' element={<VeterinarianListing />} />
            </Route>
        )
    )
  return (
      <main className=''>
          <RouterProvider router={router} />
      </main>
  )
}

export default App
