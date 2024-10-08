import { BrowserRouter, Route, Routes } from "react-router-dom";
import { LoginPage } from "./pages/loginPage";
import "./App.css";
import { CommunityPage } from "./pages/communityPage";
import { UserPage } from "./pages/userPage";
import { SharedLayout } from "./pages/shared";
import { HomePage } from "./pages/homePage";
import { CommunityCreationPage } from "./pages/createCommunity";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SharedLayout></SharedLayout>}>
          <Route index element={<LoginPage></LoginPage>}></Route>
          <Route path="/home" element={<HomePage></HomePage>}></Route>
          <Route
            path="/community/create"
            element={<CommunityCreationPage></CommunityCreationPage>}
          ></Route>
          <Route path="/user/:id" element={<UserPage></UserPage>}></Route>
          <Route
            path="/community/:id"
            element={<CommunityPage></CommunityPage>}
          ></Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
