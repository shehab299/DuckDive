const express = require("express");
const cors = require("cors");

const app = express();
app.use(cors());

app.get("/complete", (req, res) => {
  const result = {
    suggestions: [
      {
        query: "mock server",
        url: "https://www.example.com/search?q=mock+server",
      },
      {
        query: "mock server tutorial",
        url: "https://www.example.com/search?q=mock+server+tutorial",
      },
      {
        query: "mock server vs real server",
        url: "https://www.example.com/search?q=mock+server+vs+real+server",
      },
      {
        query: "mock server best practices",
        url: "https://www.example.com/search?q=mock+server+best+practices",
      },
      {
        query: "mock server tools",
        url: "https://www.example.com/search?q=mock+server+tools",
      },
    ],
  };

  res.status(200).json(result);
});

app.get("/search", (req, res) => {
  const result = {
    results: [
      {
        title: "How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "7Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "8How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "9Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "10Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "11Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "12Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "13Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "14Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "15How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "16Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "17Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "18Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "19Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "20Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "21Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "1How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "2Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "3Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "4Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "7Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "8How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "9Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "10Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "11Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "12Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "13Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "14Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "15How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "16Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "17Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "18Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "19Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "20Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "21Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "1How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "2Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "3Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "4Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "7Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "8How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "9Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "10Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "11Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "12Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "13Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "14Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "15How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "16Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "17Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "18Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "19Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "20Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "21Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "1How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "2Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "3Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "4Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "7Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "8How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "9Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "10Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "11Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "12Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "13Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "14Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
      {
        title: "15How to Build a Mock Server",
        description:
          "This comprehensive guide covers the steps to create a mock server for testing purposes. It includes detailed instructions and best practices.",
        url: "https://www.example.com/build-mock-server",
      },
      {
        title: "16Mock Server vs. Real Server: Pros and Cons",
        description:
          "Explore the advantages and disadvantages of using a mock server versus a real server in your development workflow. Learn when to use each approach.",
        url: "https://www.example.com/mock-vs-real",
      },
      {
        title: "17Testing Strategies for APIs with Mock Servers",
        description:
          "Discover effective testing strategies for APIs using mock servers. Improve your testing process and ensure reliable API performance.",
        url: "https://www.example.com/api-testing-strategies",
      },
      {
        title: "18Best Practices for Mock Server Configuration",
        description:
          "Learn the best practices for configuring a mock server to mimic real API behavior accurately. Avoid common pitfalls and optimize your testing environment.",
        url: "https://www.example.com/mock-server-best-practices",
      },
      {
        title: "19Using Mock Servers for Frontend Development",
        description:
          "Explore how mock servers can streamline frontend development by providing realistic data for UI testing. Enhance collaboration between frontend and backend teams.",
        url: "https://www.example.com/mock-servers-frontend",
      },
      {
        title: "20Security Considerations for Mock Servers",
        description:
          "Understand the security implications of using mock servers in your development process. Implement security measures to protect sensitive data during testing.",
        url: "https://www.example.com/mock-server-security",
      },
      {
        title: "21Integrating Mock Servers with Continuous Integration",
        description:
          "Discover strategies for integrating mock servers seamlessly into your CI/CD pipeline. Automate testing workflows and improve code quality with mock server integration.",
        url: "https://www.example.com/mock-server-ci-cd",
      },
    ],
  };

  res.status(200).json(result);
});

app.use((req, res) => {
  res.status(404).send("EndPoint not found");
});

app.listen(3030, () => {
  console.log("Server is running on port 3030");
});
