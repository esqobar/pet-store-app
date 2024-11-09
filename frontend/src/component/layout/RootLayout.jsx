import NavBar from "./NavBar.jsx";
import {Outlet} from "react-router-dom";
import BackgroundImageSlider from "../common/BackgroundImageSlider.jsx";

const RootLayout = () => {
    return (
        <main>
            <NavBar />
            <BackgroundImageSlider />
            <div>
                <Outlet />
            </div>
        </main>
    );
};

export default RootLayout;