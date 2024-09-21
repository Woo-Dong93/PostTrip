const LIMITED_ERROR_MESSAGE = 'LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR';

const isLimitedServiceError = (responseString: string) => {
  if (typeof responseString !== 'string') {
    return false;
  }

  return responseString.includes(LIMITED_ERROR_MESSAGE);
};

export default isLimitedServiceError;
