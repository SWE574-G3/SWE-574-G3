import { BrowserRouter, Route, Routes } from "react-router-dom";
import { LoginPage } from "./pages/loginPage";
import "./App.css";
import { CommunityPage } from "./pages/communityPage";
import { UserPage } from "./pages/userPage";
import { SharedLayout } from "./pages/shared";
import { HomePage } from "./pages/homePage";
import { CommunityCreationPage } from "./pages/createCommunity";
import { UserInterestsPage } from "./pages/UserInterests";
import PostPage from "./pages/postPage";

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
          <Route path="/community/:communityId/posts/:id" element={<PostPage></PostPage>}></Route>
          <Route
            path="/community/:id"
            element={<CommunityPage></CommunityPage>}
          ></Route>
          <Route
            path="/user/interest"
            element={<UserInterestsPage></UserInterestsPage>}
          ></Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
